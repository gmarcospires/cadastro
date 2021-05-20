package com.example.cadastro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class activity_consulta extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private ListView listViewUsuarios;
    private Button buttonFechar;
    private UsuarioAdapter usuarioAdapter;
    private ArrayList<User> users;
    private User userEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        buttonFechar = (Button)findViewById(R.id.buttonFechar);
        buttonFechar.setOnClickListener(this);

        listViewUsuarios = (ListView)findViewById(R.id.listViewUsuarios);
        listViewUsuarios.setOnItemClickListener(this);

        users = new User(this).getUsuarios();
        usuarioAdapter = new UsuarioAdapter(this, users); //adapta os usuários
        listViewUsuarios.setAdapter(usuarioAdapter);//Faz uma lista de usuários

        setTitle("Consulta Usuários");
    }
    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //Quando clica em um usuário da lista, abre a pagina de cadastro com as info do clicado
        User user = users.get(position);//Pega o usuário
        Intent intent = new Intent(this,activity_cadastro.class);//Cria um chamado para cadastro
        intent.putExtra("consulta", user.getCodigo());//Passa para a Intent o codigo do sário clicado
        userEdicao = user;
        startActivity(intent);//inicia a pagina cadastro
    }

    @Override
    protected void onResume() { //Depois de clicar num item acontece
        super.onResume();
        if (userEdicao != null){ //Se ocorreu alguma edição
            userEdicao.carregaUsuarioPeloCodigo(userEdicao.getCodigo()); //tenta carregar usuário
            if (userEdicao.isExcluir()){
                //se o usuário foi excluído
                users.remove(userEdicao); //remove o usuário da base local
            }
            usuarioAdapter.notifyDataSetChanged(); //atualiza a lista de usuários
        }
    }
}