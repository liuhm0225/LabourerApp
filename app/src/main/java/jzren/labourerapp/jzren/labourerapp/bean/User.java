package jzren.labourerapp.jzren.labourerapp.bean;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import jzren.labourerapp.jzren.labourerapp.bean.jzren.labourerapp.utils.AESUtils;

public class User {
	private String mId;
	private String mPwd;
	private static final String masterPassword = "FORYOU"; //
	private static final String JSON_ID = "user_id";
	private static final String JSON_PWD = "user_pwd";

	public User(String id, String pwd) {
		this.mId = id;
		this.mPwd = pwd;
	}

	public User(JSONObject json) throws Exception {
		if (json.has(JSON_ID)) {
			String id = json.getString(JSON_ID);
			String pwd = json.getString(JSON_PWD);
			// decode
			mId = AESUtils.decrypt(masterPassword, id);
			mPwd = AESUtils.decrypt(masterPassword, pwd);
		}
	}

	public JSONObject toJSON() throws Exception {
		// aes psw
		String id = AESUtils.encrypt(masterPassword, mId);
		String pwd = AESUtils.encrypt(masterPassword, mPwd);
		JSONObject json = new JSONObject();
		try {
			json.put(JSON_ID, id);
			json.put(JSON_PWD, pwd);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getId() {
		return mId;
	}

	public String getPwd() {
		return mPwd;
	}
}
