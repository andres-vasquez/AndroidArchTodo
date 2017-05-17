package io.github.andres_vasquez.flatfoottodo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.github.andres_vasquez.flatfoottodo.R;

/**
 * Created by andresvasquez on 5/17/17.
 */

public class TodoViewHolder extends RecyclerView.ViewHolder{

    RelativeLayout todoRelativeLayout;
    TextView todoTextView;
    ImageView checkImageView;

    public TodoViewHolder(View itemView) {
        super(itemView);

        todoRelativeLayout=(RelativeLayout) itemView.findViewById(R.id.todo_relative_layout);
        todoTextView=(TextView)itemView.findViewById(R.id.todo_text_view);
        checkImageView=(ImageView) itemView.findViewById(R.id.check_image_view);
    }
}
