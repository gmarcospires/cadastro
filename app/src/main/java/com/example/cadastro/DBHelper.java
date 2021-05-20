package com.example.cadastro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static String NOME = "sqliteappexemplo.db"; //Nome do banco
    private static int VERSAO = 1;

    public DBHelper(Context context){
        super(context,NOME,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //Cria as tabelas
        db.execSQL(
                "CREATE TABLE [usuario] (\n" +
                        "[codigo] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        "[nome] VARCHAR(60)  NOT NULL,\n" +
                        "[email] VARCHAR(60)  NOT NULL,\n" +
                        "[senha] VARCHAR(60)  NOT NULL,\n" +
                        "[imagem] BLOB  NULL\n" +
                        ")"
        );//codigo SQL do banco
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
