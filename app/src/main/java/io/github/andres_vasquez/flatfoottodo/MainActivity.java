package io.github.andres_vasquez.flatfoottodo;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.github.andres_vasquez.flatfoottodo.database.AppDatabase;
import io.github.andres_vasquez.flatfoottodo.database.TodoHelper;
import io.github.andres_vasquez.flatfoottodo.model.Todo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG = MainActivity.class.getSimpleName();

    private Context mContext;
    private FloatingActionButton mAddTodoFab;

    //Create TodoCreate popup
    private Dialog mTodoDialog;
    private EditText mTodoEditText;
    private Button mCreateTodoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

        initViews();
        mAddTodoFab.setOnClickListener(this);
        mCreateTodoButton.setOnClickListener(this);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        mAddTodoFab=(FloatingActionButton)findViewById(R.id.add_todo_fab);

        //Create popupDialog
        mTodoDialog =new Dialog(mContext);
        mTodoDialog.setContentView(R.layout.layout_create_todo);
        mTodoEditText =(EditText) mTodoDialog.findViewById(R.id.todo_edit_text);
        mCreateTodoButton =(Button) mTodoDialog.findViewById(R.id.create_todo_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_todo_fab:
                mTodoEditText.setText("");
                mTodoDialog.show();
                break;
            case R.id.create_todo_button:
                mTodoDialog.dismiss();
                createTodo(mTodoEditText.getText().toString());
                break;
            default:
                Log.w(LOG,"Unhandled click action");
                break;
        }
    }

    /**
     * Create todoTask in background
     * @param todoName TodoTask name from dialog
     */
    private void createTodo(String todoName) {
        TodoHelper todoHelper=new TodoHelper(mContext);
        todoHelper.insertTodoAsync(new Todo(todoName));
    }
}
