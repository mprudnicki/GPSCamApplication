package com.example.admin.gpscamapplication.Activities;

        import android.content.Intent;
        import android.graphics.Camera;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
import android.widget.ListView;
        import android.widget.Toast;

        import com.example.admin.gpscamapplication.R;

        import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private ListView          mainMenu;
    private ArrayAdapter      mainMenuAdapter;
    private ArrayList<String> menuContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //setTheme(R.style.MainTheme);

        mainMenu    = (ListView) findViewById(R.id.mainmenu);
        menuContent = new ArrayList<String>();

        menuContent.add("CAMERA");
        menuContent.add("GPS");
        menuContent.add("BOTH");

        mainMenuAdapter = new ArrayAdapter<String>(this, R.layout.mainmenurow, R.id.mainMenuRow, menuContent);
        mainMenu.setAdapter(mainMenuAdapter);
        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position){
                    case 0:
                        Toast.makeText(getApplicationContext(), "CAMERA", Toast.LENGTH_SHORT).show();
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(takePictureIntent, 1);
                            //REQUEST_IMAGE_CAPTURE == 1
                        }
                        onActivityResult(1,1,takePictureIntent);


                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "GPS", Toast.LENGTH_SHORT).show();
                        intent = new Intent(view.getContext(), GPS.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "CAMERA AND GPS", Toast.LENGTH_SHORT).show();
                        intent = new Intent(view.getContext(), CameraGPS.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "OPTION NOT CLICKED", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });


    }


}
