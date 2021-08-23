package Models;

import android.view.View;

public class ListViewLine {

    String header;
    String value;
    View.OnClickListener listener;

    public ListViewLine(String header, String value,View.OnClickListener _listener) {
        this.header = header;
        this.value = value;
        this.listener = _listener;
    }

    public String getHeader() {
        return header;
    }

    public String getValue() {
        return value;
    }

    public View.OnClickListener getListener() {
        return listener;
    }


}
