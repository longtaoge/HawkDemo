package org.xiangbalao.hawkdome;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.HawkBuilder.Callback;
import com.orhanobut.hawk.bean.User;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnSave;

	private Button btndel;

	private Button btnupdate;

	private Button btnQuery;

	private EditText etFirstName;

	private EditText lastname;

	private TextView tvFirstName;
	private TextView tvLastName;

	private TextView tvTextView;

	private String key = User.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nosql);

		initView();

		initDB();

	}

	private void initDB() {
		String databasesPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hawkDome.db";

		Hawk.init(MainActivity.this)
				.setStorage(
						HawkBuilder.newSqliteStorage(MainActivity.this,
								databasesPath)).setCallback(new Callback() {

					@Override
					public void onSuccess() {

						Toast.makeText(MainActivity.this, "数据库创建成功",
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onFail(Exception e) {

						Toast.makeText(MainActivity.this, "数据库创建失败",
								Toast.LENGTH_SHORT).show();

					}
				}).build();
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

		tvTextView = (TextView) findViewById(R.id.tv);
		tvTextView.setFocusable(true);

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

		// User qUser = Hawk.get(key);

		// if (qUser != null) {
		//
		// tvFirstName.setText(qUser.getFristname() + "");
		// tvLastName.setText(qUser.getLastname() + "");
		//
		// } else {
		// tvFirstName.setText("");
		// tvLastName.setText("");
		// Toast.makeText(MainActivity.this, "没有查到", Toast.LENGTH_SHORT)
		// .show();
		// }

		Hawk.<User> getObservable(key).subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<User>() {
					@Override
					public void onCompleted() {
						Log.d("rxtest", "completed");
					}

					@Override
					public void onError(Throwable e) {
						Log.d("rxtest", "error" + e.toString());
					}

					@Override
					public void onNext(User qUser) {
						if (qUser != null) {

							tvFirstName.setText(qUser.getFristname() + "");
							tvLastName.setText(qUser.getLastname() + "");

						} else {
							tvFirstName.setText("");
							tvLastName.setText("");
							Toast.makeText(MainActivity.this, "没有查到",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

	private void update() {

		save();

	}

	private void del() {

		Hawk.remove(key);

	}

	private void save() {
		User mUser = new User();
		mUser.setFristname(etFirstName.getText().toString());
		mUser.setLastname(lastname.getText().toString());

		Hawk.putObservable(key, mUser).subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {
						Log.i("rxtest", "onCompleted");
					}

					@Override
					public void onError(Throwable e) {
						Log.i("rxtest", "onError" + e.toString());
					}

					@Override
					public void onNext(Boolean s) {
						Log.i("rxtest", "onNext" + s);
					}
				});

		// Hawk.put(key, mUser);

	}

}
