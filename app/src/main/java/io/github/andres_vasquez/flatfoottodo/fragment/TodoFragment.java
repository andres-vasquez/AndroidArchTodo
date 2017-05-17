package io.github.andres_vasquez.flatfoottodo.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.andres_vasquez.flatfoottodo.R;
import io.github.andres_vasquez.flatfoottodo.adapter.TodoAdapter;
import io.github.andres_vasquez.flatfoottodo.model.Todo;
import io.github.andres_vasquez.flatfoottodo.viewModel.TodoViewModel;

/**
 * Created by andresvasquez on 5/16/17.
 */

public class TodoFragment extends LifecycleFragment{
    private static final String LOG = TodoFragment.class.getSimpleName();

    private Context mContext;
    private RecyclerView mTodoRecyclerView;
    private TodoAdapter mTodoAdapter;

    private TodoViewModel mTodoViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.todo_fragment, container, false);
        mTodoRecyclerView=(RecyclerView)view.findViewById(R.id.todo_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTodoAdapter = new TodoAdapter(mContext);
        mTodoRecyclerView.setAdapter(mTodoAdapter);

        //Init Recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        mTodoRecyclerView.setLayoutManager(linearLayoutManager);

        //Add divider decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,
                linearLayoutManager.getOrientation());
        mTodoRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        subscribeEvents();
    }

    /**
     * Add observer to TodoViewModel
     */
    private void subscribeEvents() {
        mTodoViewModel.getTodoList().observe(this,todoObserver);
    }

    /**
     * todoObserver method to react to TodoTable changes
     */
    final Observer<List<Todo>> todoObserver = new Observer<List<Todo>>() {
        @Override
        public void onChanged(@Nullable final List<Todo> todoList) {
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

}
