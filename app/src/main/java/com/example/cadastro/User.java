package com.example.cadastro;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class User {
    //Campos tabela
    private int codigo;
    private String nome;
    private String email;
    private String senha;
    //Auxilio
    private byte[] imagem;
    private Bitmap avatar;
    private String urlGravatar;
    private boolean excluir;
    private Context context;

    //Construtor que recebe um contexto
    public User(Context context){
        this.context = context;
        codigo = -1; //usuário ainda n cadastrado
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.urlGravatar = String.format("https://s.gravatar.com/avatar/%s?s=200",Auxilio.md5Hex(this.email));
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
        if (this.imagem != null)
            this.avatar = Auxilio.getImagemBytes(this.imagem);
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getUrlGravatar() {
        return urlGravatar;
    }

    public void setUrlGravatar(String urlGravatar) {
        this.urlGravatar = urlGravatar;
    }

    public boolean isExcluir() {
        return excluir;
    }

    public void setExcluir(boolean excluir) {
        this.excluir = excluir;
    }

    public boolean excluir(){
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();//alterar o bd
            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.delete("usuario","codigo = ?",new String[]{String.valueOf(codigo)}); //Delete o bd

            excluir = true;

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            sqLiteDatabase.endTransaction();
            return false;
        }finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
    }

    public boolean salvar(){ //Insert ou Update na bd
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();//alterar o bd
            String sql = "";
            if (codigo == -1){ //diferencia se é um update ou insert
                                            //1,2,3,4
                sql = "INSERT INTO usuario (nome,email,senha,imagem) VALUES (?,?,?,?)"; //Inserir no bd
            }else{
                                                                                            //5
                sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, imagem = ? WHERE codigo = ?"; //Update no bd
            }
            sqLiteDatabase.beginTransaction(); //inicia uma transação
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql); //
            sqLiteStatement.clearBindings();//limpa variaveis do sqLiteStatement
            //Quais os valores dos parâmetros (?)
            sqLiteStatement.bindString(1,nome);
            sqLiteStatement.bindString(2,email);
            sqLiteStatement.bindString(3,senha);
            if (imagem != null){
                sqLiteStatement.bindBlob(4,imagem);
            }
            if (codigo != -1){
                sqLiteStatement.bindString(5,String.valueOf(codigo));
            }
            sqLiteStatement.executeInsert(); //Executa o insert/update
            sqLiteDatabase.setTransactionSuccessful(); //transação com sucesso
            sqLiteDatabase.endTransaction();//acaba a transação
            return true;
        }catch (Exception e){
            e.printStackTrace();
            sqLiteDatabase.endTransaction();//acaba a transação
            return false;
        }finally {//encerra conexão
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
    }

    public ArrayList<User> getUsuarios(){
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<User> users = new ArrayList<>();
        try{
            dbHelper = new DBHelper(context); //Instancia o db
            sqLiteDatabase = dbHelper.getReadableDatabase(); //Apenas le o banco
                                            // tabela       coluna      seleção ("codigo = ?")   argumento de seleção(new String[]{"1"}) agrupamento having ordenar
            cursor = sqLiteDatabase.query("usuario",null,null,null,null,null,null); //Select * from usuário
            while (cursor.moveToNext()){ //Consulta a consulta até o último usuário
                User user = new User(context);//Cria um novo usuário
                user.codigo = cursor.getInt(cursor.getColumnIndex("codigo")); //acessa solunas
                user.nome = cursor.getString(cursor.getColumnIndex("nome"));
                user.senha = cursor.getString(cursor.getColumnIndex("senha"));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setImagem(cursor.getBlob(cursor.getColumnIndex("imagem")));
                users.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally { //fechar as coneções, pois n pode ter mais de 1
            if ((cursor != null) && (!cursor.isClosed()))
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
        return users;
    }

    public void carregaUsuarioPeloCodigo(int codigo){//Carrega um usuário do bd
        DBHelper dbHelper = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try{
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getReadableDatabase(); //Le o bd
            cursor = sqLiteDatabase.query("usuario",null,"codigo = ?",new String[]{String.valueOf(codigo)},null,null,null);//Select * from Usuário where codigo = ncodigo;
            excluir = true;
            while (cursor.moveToNext()){
                this.codigo = cursor.getInt(cursor.getColumnIndex("codigo"));
                nome = cursor.getString(cursor.getColumnIndex("nome"));
                senha = cursor.getString(cursor.getColumnIndex("senha"));
                setEmail(cursor.getString(cursor.getColumnIndex("email")));
                setImagem(cursor.getBlob(cursor.getColumnIndex("imagem")));
                excluir = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if ((cursor != null) && (!cursor.isClosed()))
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
            if (dbHelper != null)
                dbHelper.close();
        }
    }

}