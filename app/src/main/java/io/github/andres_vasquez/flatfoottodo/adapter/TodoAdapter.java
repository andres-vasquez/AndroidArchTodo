package io.github.andres_vasquez.flatfoottodo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.andres_vasquez.flatfoottodo.R;
import io.github.andres_vasquez.flatfoottodo.database.TodoHelper;
import io.github.andres_vasquez.flatfoottodo.model.OnClickTodo;
import io.github.andres_vasquez.flatfoottodo.model.Todo;
import io.github.andres_vasquez.flatfoottodo.utils.Constants;

/**
 * Created by andresvasquez on 5/17/17.
 */

/**
 * Todos adapter to fill Recicler View with Todos list data from ROOM database
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder>{

    private Context mContext;
    private List<Todo> items;
    private List<Todo> itemsFiltered;
    private LayoutInflater layoutInflater;
    private OnClickTodo editCallback;
    private int mFilter = Constants.FILTER_ALL;

    /**
     * Todos Adapter constructor
     * @param context Fragment context
     */
    public TodoAdapter(Context context){
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.items=new ArrayList<Todo>();
        this.itemsFiltered=new ArrayList<Todo>();
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.todo, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, final int position) {
        final Todo todo=itemsFiltered.get(position);
        holder.todoTextView.setText(todo.getName());

        //Hide or show check image
        if(todo.isFinished()){
            holder.finishedCheckBox.setChecked(true);
        } else {
            holder.finishedCheckBox.setChecked(false);
        }

        //Add click listener and change the value in background
        holder.finishedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setFinished(!todo.isFinished());
                TodoHelper todoHelper=new TodoHelper(mContext);
                todoHelper.updateTodoAsync(todo);
            }
        });

        //Add click listener and editCallback to return edit action
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editCallback !=null){
                    editCallback.onClick(todo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    /**
     * SWAP list from database into adapter
     * @param todoList todos List from database
     */
    public void swap(List<Todo> todoList) {
        this.items.clear();
        this.items.addAll(todoList);

        filterList(mFilter);
    }

    /**
     * Filter values
     * @param filter
     */
    public void filterList(Integer filter) {
        this.mFilter=filter;

        if(filter == Constants.FILTER_ALL){
            this.itemsFiltered.clear();
            this.itemsFiltered.addAll(this.items);
            notifyDataSetChanged();
        } else {
            this.itemsFiltered.clear();
            for (Todo todo : items){
                if(filter==Constants.FILTER_FINISHED && todo.isFinished()){
                    this.itemsFiltered.add(todo);
                } else if(filter==Constants.FILTER_PENDING && !todo.isFinished()){
                    this.itemsFiltered.add(todo);
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * Add click edit callback action
     * @param editCallback onClickTodo listener
     */
    public void setEditCallback(OnClickTodo editCallback) {
        this.editCallback = editCallback;
    }
}

