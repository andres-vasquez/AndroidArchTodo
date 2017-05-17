package io.github.andres_vasquez.flatfoottodo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.andres_vasquez.flatfoottodo.R;
import io.github.andres_vasquez.flatfoottodo.database.TodoHelper;
import io.github.andres_vasquez.flatfoottodo.model.Todo;

/**
 * Created by andresvasquez on 5/17/17.
 */

/**
 * Todos adapter to fill Recicler View with Todos list data from ROOM database
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder>{

    private Context mContext;
    private List<Todo> items;
    private LayoutInflater layoutInflater;

    /**
     * Todos Adapter constructor
     * @param context Fragment context
     */
    public TodoAdapter(Context context){
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.items=new ArrayList<Todo>();
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.todo, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, final int position) {
        final Todo todo=items.get(position);
        holder.todoTextView.setText(todo.getName());

        //Hide or show check image
        if(todo.isFinished()){
            holder.checkImageView.setVisibility(View.VISIBLE);
        } else {
            holder.checkImageView.setVisibility(View.GONE);
        }

        //Add click listener and change the value in background
        holder.todoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setFinished(!todo.isFinished());
                TodoHelper todoHelper=new TodoHelper(mContext);
                todoHelper.updateTodoAsync(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * SWAP list from database into adapter
     * @param todoList todos List from database
     */
    public void swap(List<Todo> todoList) {
        this.items.clear();
        this.items.addAll(todoList);
        notifyDataSetChanged();
    }
}

