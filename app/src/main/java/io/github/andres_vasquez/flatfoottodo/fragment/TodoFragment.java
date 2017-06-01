package io.github.andres_vasquez.flatfoottodo.fragment;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import io.github.andres_vasquez.flatfoottodo.MainActivity;
import io.github.andres_vasquez.flatfoottodo.R;
import io.github.andres_vasquez.flatfoottodo.adapter.TodoAdapter;
import io.github.andres_vasquez.flatfoottodo.database.TodoHelper;
import io.github.andres_vasquez.flatfoottodo.model.OnClickTodo;
import io.github.andres_vasquez.flatfoottodo.model.Todo;
import io.github.andres_vasquez.flatfoottodo.viewModel.TodoViewModel;

/**
 * Created by andresvasquez on 5/16/17.
 */

public class TodoFragment extends Fragment implements OnClickTodo{
    private static final String LOG = TodoFragment.class.getSimpleName();

    private MainActivity mActivity;
    private Context mContext;
    private RecyclerView mTodoRecyclerView;
    private TodoAdapter mTodoAdapter;

    private TodoViewModel mTodoViewModel;

    private Dialog mTodoDialog;
    private EditText mTodoEditText;
    private Button mCreateTodoButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

        if(context instanceof MainActivity){
            mActivity=(MainActivity)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.todo_fragment, container, false);
        mTodoRecyclerView=(RecyclerView)view.findViewById(R.id.todo_recycler_view);

        //Create popupDialog
        mTodoDialog =new Dialog(mContext);
        mTodoDialog.setContentView(R.layout.layout_create_todo);
        mTodoEditText =(EditText) mTodoDialog.findViewById(R.id.todo_edit_text);
        mCreateTodoButton =(Button) mTodoDialog.findViewById(R.id.create_todo_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCreateTodoButton.setText(getString(R.string.update_todo));

        mTodoAdapter = new TodoAdapter(mContext);
        mTodoAdapter.setClickCallback(this);
        mTodoRecyclerView.setAdapter(mTodoAdapter);

        //Init Recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        mTodoRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTodoViewModel = ViewModelProviders.of(mActivity).get(TodoViewModel.class);
        subscribeEvents();
    }

    /**
     * Add observer to TodoViewModel
     */
    private void subscribeEvents() {
        mTodoViewModel.getTodoList().observe(mActivity,todoObserver);
        mTodoViewModel.getmFilter().observe(mActivity,filterObserver);
    }

    /**
     * todoObserver method to react to TodoTable changes
     */
    final Observer<List<Todo>> todoObserver = new Observer<List<Todo>>() {
        @Override
        public void onChanged(@Nullable final List<Todo> todoList) {
            Log.i(LOG,"todoObserver");
            if(todoList!=null){
                //Post TodoList items in MainThread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mTodoAdapter.swap(todoList);
                    }
                });
            }
        }
    };

    /**
     * filterObserver method to react to filter variable changes
     */
    final Observer<Integer> filterObserver = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable final Integer filter) {
            Log.i(LOG,"filterObserver");
            mTodoAdapter.filterList(filter);
        }
    };

    @Override
    public void onEditClick(final Todo todo) {
        mTodoEditText.setText(todo.getName());
        mCreateTodoButton.setOnClickListener(null);
        mCreateTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setName(mTodoEditText.getText().toString());
                TodoHelper todoHelper=new TodoHelper(mContext);
                todoHelper.updateTodoAsync(todo);
                mTodoDialog.dismiss();
            }
        });
        mTodoDialog.show();
    }

    @Override
    public void onDeleteClick(Todo todo) {
        TodoHelper todoHelper=new TodoHelper(mContext);
        todoHelper.deleteTodoAsync(todo);
        Toast.makeText(mContext,getString(R.string.delete_message),Toast.LENGTH_SHORT).show();
    }
}
