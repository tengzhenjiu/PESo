package Thread;

/*通用向服务器发送消息的类*/
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peso.socket;

import dbmodels.Publication;

public class CommThread extends Thread {
	private String msg = "";
	public String recMsg = "";
	Socket Soc = new Socket();
	socket soc = new socket();
	public ArrayList<Publication> publist = new ArrayList<Publication>();

	public CommThread(String Msg) {
		msg = Msg;
	}

	@Override
	public void run() {
		try {
			if (!publist.isEmpty()) {
				publist.clear();
			}
			Soc = soc.connecttoserver();
			soc.SendMsg(Soc, msg);
			recMsg = soc.ReceiveMsg(Soc);
			if (!(recMsg == null) || !(recMsg == "")) {
				if (recMsg.length() > 100) {
					Gson gson = new Gson();
					publist = gson.fromJson(
							recMsg,
							new TypeToken<ArrayList<Publication>>() {
							}.getType());
					Log.i("yes", "解析成功");
					Log.i("数量", recMsg.length() + "");
				}
			}
			soc.CloseSocket(Soc);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}