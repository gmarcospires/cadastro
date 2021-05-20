package com.example.cadastro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UsuarioAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;

    public UsuarioAdapter(@NonNull Context context, @NonNull ArrayList<User> users) { //Construtor da classe
        super(context, 0, users);
        this.users = users;
    }



    @NonNull
    @Override
                        //Index(users)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //Devolve a view item usuário corretamente
        User user = users.get(position);//Pega um user

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_usuario,null); //Atribui um layout para contexto dessa classe

        //Converte
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageViewAvatar);
        TextView textViewNome = (TextView)convertView.findViewById(R.id.textViewNome);
        TextView textViewEmail = (TextView)convertView.findViewById(R.id.textViewEmail);
        //Coloca os dados nas views convertidas
        textViewNome.setText(user.getNome().toString());
        textViewEmail.setText(user.getEmail().toString());
        if (user.getAvatar() != null){
            imageView.setImageBitmap(user.getAvatar()); //Convervte
        }

        return  convertView;
    }
}
