package org.xiangbalao.simplenosqldome;

import java.sql.Date;
import java.util.List;

import org.w3c.dom.Text;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.colintmiller.simplenosql.bean.SampleBean;
import com.colintmiller.simplenosql.bean.User;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.Fragment.SavedState;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnSave;

	private Button btndel;

	private Button btnupdate;

	private Button btnQuery;

	private EditText etFirstName;

	private EditText lastname;

	private TextView tvFirstName;
	private TextView tvLastName;

	private String bucketId = "bucketId";

	private String entityId = "entityId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nosql);

		initView();

	}

	private void initView() {

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		btndel = (Button) findViewById(R.id.btndel);
		btndel.setOnClickListener(this);
		btnupdate = (Button) findViewById(R.id.btnupdate);
		btnupdate.setOnClickListener(this);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		btnQuery.setOnClickListener(this);

		etFirstName = (EditText) findViewById(R.id.etFirstName);

		lastname = (EditText) findViewById(R.id.lastname);

		tvFirstName = (TextView) findViewById(R.id.tvFirstName);
		tvLastName = (TextView) findViewById(R.id.tvLastName);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSave:
			save();
			break;
		case R.id.btndel:
			del();
			break;
		case R.id.btnupdate:
			update();
			break;
		case R.id.btnQuery:
			query();
			break;
		default:
			break;
		}

	}

	private void query() {
		NoSQL.with(MainActivity.this).using(User.class).bucketId(bucketId)
				.entityId(entityId).retrieve(new RetrievalCallback<User>() {

					@Override
					public void retrievedResults(
							List<NoSQLEntity<User>> entities) {

						if (entities.size() > 0) {
							User ruUser = entities.get(0).getData();

							tvFirstName.setText(ruUser.getFristname() + "");
							tvLastName.setText(ruUser.getLastname() + "");
							Toast.makeText(MainActivity.this,
									entities.size() + "", Toast.LENGTH_SHORT)
									.show();
						} else {
							tvFirstName.setText("");
							tvLastName.setText("");
							Toast.makeText(MainActivity.this, "没有查到",
									Toast.LENGTH_SHORT).show();
						}

					}
				}

				);

	}

	private void update() {

		save();

	}

	private void del() {
		NoSQL.with(MainActivity.this).using(User.class).bucketId(bucketId)
				.entityId(entityId).delete();

	}

	private void save() {

		final NoSQLEntity<User> entity = new NoSQLEntity<User>(bucketId,
				entityId);
		User data = new User();
		data.setFristname(etFirstName.getText().toString());
		data.setLastname(lastname.getText().toString());
		entity.setData(data);

		NoSQL.with(getApplicationContext()).using(User.class).save(entity);

	}

}
