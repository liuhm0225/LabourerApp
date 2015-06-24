package jzren.labourerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initMenu();
    }

    private void initMenu() {
        GridView gridview = (GridView) findViewById(R.id.GridView);
        List<Map<String, Object>> meumList = new ArrayList< Map<String, Object> >();
        for(int i = 1;i < 10;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.mipmap.ic_launcher);
            map.put("ItemText", ""+i);
            meumList.add(map);
        }
        SimpleAdapter saItem = new SimpleAdapter(this,
                meumList,
                R.layout.menuitem,
                new String[]{"ItemImage","ItemText"},
                new int[]{R.id.menuItemImage,R.id.menuItemName});


        gridview.setAdapter(saItem);

        gridview.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
                    {
                        int index=arg2+1;//id是从0开始的，所以需要+1
                        Toast.makeText(getApplicationContext(), "你按下了选项：" + index, Toast.LENGTH_SHORT).show();
                        //Toast用于向用户显示一些帮助/提示
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.setClass(MenuActivity.this, LoginActivity.class);
        startActivity(it);
        MenuActivity.this.finish();
    }
}
