package com.example.prepay.test_db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class UserDBHelper(context: Context) :
    SQLiteOpenHelper(context, "log_in_test", null, 1 ) {

    // users 테이블 생성
    override fun onCreate(testDB : SQLiteDatabase?) {
        testDB?.execSQL("create Table users(id TEXT primary key, password TEXT, nick TEXT)")
    }

    // 데이터베이스 버전이 변경되었을 때 테이블 삭제 후 재생성
    override fun onUpgrade(testDB : SQLiteDatabase?, old: Int, new: Int) {
       testDB?.execSQL("DROP TABLE IF EXISTS users")
       onCreate(testDB)
    }

    // id, password, nick 삽입 (성공시 true, 실패시 false)
    fun insertData( id: String, password: String, nick: String): Boolean {
        val testDB = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("id", id)
            put("password", password)
            put("nick", nick)
        }
        val result = testDB.insert("users", null, contentValues)
        testDB.close()
        return result != -1L
    }

    // 사용자 아이디가 존재하는지 확인
    fun checkId(id: String) : Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", arrayOf(id))
        val isExist = cursor.count > 0
        cursor.close()
        db.close()
        return isExist
    }

    // 사용자 닉네임이 존재하는지 확인
    fun checkNick(nick: String) : Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE nick = ?", arrayOf(nick))
        val isExist = cursor.count > 0
        cursor.close()
        db.close()
        return isExist
    }

    // 사용자 닉네임이 존재하는지 확인
    fun checkUserPassword(id: String, password: String) : Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ? AND password = ?", arrayOf(id, password))
        val isExist = cursor.count > 0
        cursor.close()
        db.close()
        return isExist
    }

    // DB name을 log_in_test.db로 설정
    companion object {
        const val LOG_IN_TEST = "log_in_test.db"
    }
}


