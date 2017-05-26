package io.github.andres_vasquez.flatfoottodo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.andres_vasquez.flatfoottodo.database.TodoHelper;
import io.github.andres_vasquez.flatfoottodo.model.GetCallback;
import io.github.andres_vasquez.flatfoottodo.model.Todo;
import io.github.andres_vasquez.flatfoottodo.utils.Constants;
import io.github.andres_vasquez.flatfoottodo.utils.GlobalFunctions;

public class MainActivity extends TimerActivity implements View.OnClickListener{

    private static final String LOG = MainActivity.class.getSimpleName();

    private Context mContext;
    private Toolbar mToolbar;
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
        setSupportActionBar(mToolbar);

        mAddTodoFab.setOnClickListener(this);
        mCreateTodoButton.setOnClickListener(this);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mAddTodoFab=(FloatingActionButton)findViewById(R.id.add_todo_fab);

        timerActionImageView =(ImageView)findViewById(R.id.timer_action_image_view);
        timerTextView =(TextView)findViewById(R.id.timer_text_view);

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
            case R.id.timer_action_image_view:
                super.onClick(v);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actiion_filter:
                showFiltersPopup();
                return true;
            case R.id.actiion_share:
                shareAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Show filters Popup
     */
    private void showFiltersPopup() {
        int filterSelected=todoViewModel.getmFilter().getValue();
        String[] arrPickerItems=new String[]{
                getString(R.string.state_all),
                getString(R.string.state_pending),
                getString(R.string.state_finished)};

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setSingleChoiceItems(arrPickerItems, filterSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoViewModel.getmFilter().setValue(which);
                dialog.dismiss();
            }
        });
        AlertDialog dialog=alert.create();
        GlobalFunctions.fixDialogMarshmellow(dialog);
        dialog.show();
    }

    /**
     * Share action method
     */
    private void shareAction() {
        TodoHelper todoHelper = new TodoHelper(mContext);
        todoHelper.getTodoForExport(new GetCallback() {
            @Override
            public void onLoad(List<Todo> todoList) {
                String shareBody = "";
                for (Todo todo : todoList){
                    shareBody+=todo.getName()+ " isFinished: "+todo.isFinished()+"\n";
                }

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        getResources().getString(R.string.share_subject));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });
    }
}
