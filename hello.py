from flask import Flask, request, jsonify
import json
import pymysql
import subprocess
import socket
import pandas as pd
from gensim.models import fasttext

import os

#Flask 인스턴스 생성
from sklearn.preprocessing import LabelEncoder

app = Flask(__name__)

def getConnection():
    return pymysql.connect(host='holy97.cafe24.com', port=3306, user='holy97',
                           password='Pwd2580**', db='holy97', charset='utf8',
                           autocommit=True, cursorclass=pymysql.cursors.DictCursor,
                           connect_timeout=100)

def count_comment(token_data):
    unique_comment_tokenized = [list(i) for i in set(tuple(i) for i in token_data)]
    word_dic = {}

    # word count
    for words in unique_comment_tokenized:
        for word in words:
            if not (word in word_dic):
                word_dic[word] = 0
                word_dic[word] += 1

    keys = sorted(word_dic.items(), key=lambda x: x[1], reverse=True)
    for word, count in keys[:50]:
        print("{0}({1}) ".format(word, count), end="")

    # [] 없애주는 코드
    from itertools import chain
    words = set(chain(*unique_comment_tokenized))

    n_vocab = len(words)
    print("")
    print("Total Vocab: ", n_vocab)
    print("")

    return keys, n_vocab


def emotion():
    title = request.form['title']
    comment = request.form['content']
    uid = request.form['uid']

    from konlpy.tag import Mecab
    mecab = Mecab("C:\mecab\mecab-ko-dic")
    token_data = []
    token = mecab.nouns(str(comment))
    token_data.append(token)
    series_token_data = pd.Series(token_data)

    # 모델 불러오기
    fastText_model = fasttext.FastText.load("./embedding/tweet_fastText_0717.model")
    docs_vectors_ft = pd.DataFrame()
    for doc in series_token_data:
        temp = pd.DataFrame()
        for word in doc:
            ft = fastText_model[word]
            temp = temp.append(pd.Series(ft), ignore_index=True)
        # take the average of each column(w0, w1, w2,........w300)
        doc_vector_ft = temp.mean()
        # append each document value to the final dataframe
        docs_vectors_ft = docs_vectors_ft.append(doc_vector_ft, ignore_index=True)

    from sklearn.externals import joblib
    # pickled binary file 형태로 저장된 객체를 로딩한다
    file_name = './embedding/tweet_bagg_SVM.pkl'
    model = joblib.load(file_name)

    pred = model.predict(docs_vectors_ft)

    # print("DB 연결중")
    conn = getConnection()
    curs = conn.cursor()

    sql = "UPDATE emo_board SET EBEMO = '"+pred[0]+"' WHERE EBTITLE = '"\
          +title+"' AND EBCONTENT = '"+comment+"' AND UID = '"+uid+"'"
    curs.execute(sql)
    curs.close()
    conn.close()
    return "성공"

    #proc = subprocess.Popen(["D:\PHP\php", "./BookList_Insert.php"], shell=True, stdout=subprocess.PIPE)
    #script_response = proc.stdout.read()

    #return script_response

def similarity():
    conn = getConnection()
    cursor = conn.cursor()

    btitle = request.form['btitle']
    authors = request.form['authors']
    thumbnail = request.form['thumbnail']

    sql = "SELECT BID FROM book_list WHERE BTITLE = '"+btitle+\
          "' AND BAUTHOR = '"+authors+"' AND BIMAGE = '"+thumbnail+"'"
    cursor.execute(sql)
    bid = str(cursor.fetchone()['BID'])

    sql = "SELECT BID, UID, EBEMO FROM emo_board"
    cursor.execute(sql)

    result = cursor.fetchall()

    df = pd.DataFrame(result)
    df.dropna()
    for col in df.columns:
        df[col] = df[col].map(lambda x: str(x).lstrip("b'").rstrip("'"))

    le = LabelEncoder()
    df['EBEMO'] = le.fit_transform(df['EBEMO'])

    book_user_emotion = df.pivot_table('EBEMO', index='BID', columns='UID')
    book_user_emotion.fillna(0, inplace=True)

    from sklearn.metrics.pairwise import cosine_similarity
    ibc = cosine_similarity(book_user_emotion)
    ibc = pd.DataFrame(data=ibc, index=book_user_emotion.index,
                                       columns=book_user_emotion.index)

    def get_item_based_collabor(title):
        print(ibc)
        return ibc[title].sort_values(ascending=False)[1:6]

    tmp = get_item_based_collabor(bid)

    cursor.execute("set names utf8")
    cursor.execute("SELECT BID, BTITLE, BAUTHOR, BIMAGE FROM book_list WHERE " \
          "BID IN (" + ",".join(('%s',)*len(tmp)) + ")", tuple(tmp.index))
    result = cursor.fetchall()
    conn.close()

    df2 = pd.DataFrame(result)
    tmp = tmp.to_frame().reset_index()
    tmp['BID'] = tmp['BID'].astype(int)
    resultDf = pd.merge(df2, tmp).sort_values(by=[bid], ascending=False)
    print(resultDf[bid])

    resultArray = []
    for index, row in resultDf.iterrows():
        arr = {"bid":row[0], "title":row[1], "authors":row[2],
               "thumbnail":row[3], "similarity":row[4]}
        resultArray.append(arr)
    #print(resultArray)

    return jsonify({"documents":resultArray})

def daySelect():
    conn = getConnection()
    cursor = conn.cursor()

    uid = request.form['uid']

    sql = "SELECT * FROM calendar WHERE UID = '"+uid+"'"
    cursor.execute(sql)
    result = cursor.fetchall()

    df = pd.DataFrame(result)
    for col in df.columns:
        df[col] = df[col].map(lambda x: str(x).lstrip("b'").rstrip("'"))

    print(list(df['RDATE']))
    return jsonify({"documents":list(df['RDATE'])})

@app.route("/")
def print_hello():
    print(socket.gethostbyname(socket.getfqdn()))
    print(os.getcwd())
    return "Hello World - Flask"

@app.route("/Emotion_Analysis", methods=['GET', 'POST'])
def emotionAnalysis():
    return emotion()

@app.route("/Similarity_Analysis", methods=['GET', 'POST'])
def similarityAnalysis():
    return similarity()

@app.route("/ReadDays_Select", methods=['GET', 'POST'])
def readDaysSelect():
    return daySelect()


if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)
    #app.run(debug=True)
