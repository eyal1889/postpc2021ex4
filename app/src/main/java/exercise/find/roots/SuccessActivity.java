package exercise.find.roots;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succsess);
        TextView original_text = findViewById(R.id.original_num);
        TextView root1_text = findViewById(R.id.root1);
        TextView root2_text = findViewById(R.id.root2);
        TextView time_text = findViewById(R.id.time_1);

        TextView original_text_text = findViewById(R.id.original_text);
        TextView time_text_text = findViewById(R.id.time_second);
        TextView root1_text_text = findViewById(R.id.text_root1);
        TextView root2_text_text = findViewById(R.id.text_root2);
        original_text_text.setText("original number:");
        time_text_text.setText("time taken (in sec):");
        root1_text_text.setText("root1:");
        root2_text_text.setText("root2:");
        Intent createdMe = getIntent();
        long original=0;
        long root1=0;
        long root2=0;
        long time=0;
        if(createdMe.hasExtra("original")){
            original = createdMe.getLongExtra("original",0);
        }
        if(createdMe.hasExtra("root1")){
            root1 = createdMe.getLongExtra("root1",0);
        }
        if(createdMe.hasExtra("root2")){
            root2 = createdMe.getLongExtra("root2",0);
        }
        if(createdMe.hasExtra("time")){
            time = createdMe.getLongExtra("time",0);
        }
        original_text.setText(String.format("%d",original));
        root1_text.setText(String.format("%d",root1));
        root2_text.setText(String.format("%d",root2));
        time_text.setText(String.format("%d",time));
    }
}
