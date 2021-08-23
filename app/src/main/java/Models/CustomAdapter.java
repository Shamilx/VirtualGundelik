package Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shamil.virtualgundelik.R;
import java.util.List;

public class CustomAdapter extends BaseAdapter {


    List<ListViewLine> lines;
    LayoutInflater inflater;
    Context context;

    public CustomAdapter(Context _context, List<ListViewLine> _lines) {
        this.lines = _lines;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return lines.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = LayoutInflater.from(context);
        View line = inflater.inflate(R.layout.list_view_style,null);
        TextView header = line.findViewById(R.id.list_view_header);
        TextView value = line.findViewById(R.id.list_view_value);


        ListViewLine myLine =  lines.get(i);

        header.setText(myLine.getHeader());
        value.setText(myLine.getValue());
        line.setClickable(true);
        line.setActivated(true);
        line.setOnClickListener(myLine.getListener());

        return line;
    }
}
