package com.example.cadastro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class activity_cadastro extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonExcluir;
    private Button buttonSalvar;
    private Button buttonCancelar;
    private ImageView imageView;

    private final User user = new User(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro);
        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextSenha = (EditText)findViewById(R.id.editTextSenha);
        buttonExcluir = (Button)findViewById(R.id.buttonExcluir);
        buttonCancelar = (Button)findViewById(R.id.buttonCancelar);
        buttonSalvar = (Button)findViewById(R.id.buttonSalvar);
        imageView = (ImageView)findViewById(R.id.imageViewAvatar);

        buttonExcluir.setOnClickListener(this);
        buttonSalvar.setOnClickListener(this);
        buttonCancelar.setOnClickListener(this);

        if (getIntent().getExtras() != null){ //Vem da tela de consulta? Extras (putExtra)
            setTitle(getString(R.string.titulo_editando)); //Seta o titulo da activity
            int codigo = getIntent().getExtras().getInt("consulta"); //Pega dado do extra
            user.carregaUsuarioPeloCodigo(codigo); //Carrega os dados do usuário pelo codigo recebido pelo extra
          //Coloca os valores do usuário nos elementos da view
            if (user.getAvatar() != null){
                imageView.setImageBitmap(user.getAvatar());
            }
            editTextNome.setText(user.getNome().toString());
            editTextEmail.setText(user.getEmail().toString());
            editTextSenha.setText(user.getSenha().toString());
        }else{
            setTitle(getString(R.string.titulo_incluindo)); //Seta o titulo da activity
        }

        buttonExcluir.setEnabled(true);
        if (user.getCodigo() == -1){ //Se o usuário ainda n foi criado, não há como excluir
            buttonExcluir.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) { //Define as ações onClick(), por meio do id do botão
        switch (v.getId()){
            case R.id.buttonCancelar : {//cancela
                finish(); // fecha a activity
                break;
            }
            case R.id.buttonExcluir : { //Exclui todos os dados
                user.excluir(); //exclui o user
                finish();
                break;
            }
            case R.id.buttonSalvar :{ //salva o user
                boolean valido = true;
                user.setNome(editTextNome.getText().toString().trim());
                user.setEmail(editTextEmail.getText().toString().trim().toLowerCase());
                user.setSenha(editTextSenha.getText().toString().trim());
                carregaImagem(); //carregar algo da internet, não pode ser na thread principal
                //Mensagem de erro para o user, campos obrigatorios
                if (user.getNome().equals("")){
                    editTextNome.setError(getString(R.string.obrigatorio));
                    valido = false;
                }

                if (user.getEmail().equals("")){
                    editTextEmail.setError(getString(R.string.obrigatorio));
                    valido = false;
                }

                if (user.getSenha().equals("")){
                    editTextSenha.setError(getString(R.string.obrigatorio));
                    valido = false;
                }

                if (valido){
                    user.salvar(); //salva o usuário
                    finish(); // fecha a activity
                }
                break;
            }
        }
    }

    private void carregaImagem(){
        //Cria uma nova thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Faz o dowload da imagem
                user.setImagem(Auxilio.getImagemBytesFromUrl(user.getUrlGravatar()));
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(user.getAvatar());
                    } //coloca a imagem na view
                });
            }
        });
        thread.start(); //inicia a thread
        try{
            thread.join(); //aguarda a thread acabar ATENÇÃO!Pode dar problema
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}