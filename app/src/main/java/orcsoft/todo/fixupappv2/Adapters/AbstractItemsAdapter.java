package orcsoft.todo.fixupappv2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orcsoft.todo.fixupappv2.Entity.AbstractItem;
import orcsoft.todo.fixupappv2.R;

public class AbstractItemsAdapter extends BaseAdapter {
    private List<AbstractItem> items = new ArrayList<AbstractItem>();
    private Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
    private Context context;

    public AbstractItemsAdapter(List<AbstractItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AbstractItem abstractItem = items.get(position);
        final Integer count;
        if (!counts.containsKey(position)) {
            counts.put(position, 1);
            count = 1;
            ((OnAdapterInteractionListener) context).onAdapterInteraction();
        } else {
            count = counts.get(position);
        }

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_view, null);
        }

        ((TextView) convertView.findViewById(R.id.item_name)).setText(abstractItem.getTitle() + " (" + abstractItem.getCost() + ").");
        ((TextView) convertView.findViewById(R.id.item_count)).setText(String.valueOf(count));
        ((TextView) convertView.findViewById(R.id.item_cost)).setText(String.valueOf(counts.get(position) * abstractItem.getCost()));
        ((TextView) convertView.findViewById(R.id.item_unit_title)).setText(String.valueOf(abstractItem.getUnit_title_short()));

        convertView.findViewById(R.id.button_inc).setTag(position);
        convertView.findViewById(R.id.button_dec).setTag(position);
        convertView.findViewById(R.id.button_del).setTag(position);

        convertView.findViewById(R.id.button_inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                Integer currentCount = counts.get(position);
                counts.put(position, currentCount + 1);
                reload();
            }
        });

        convertView.findViewById(R.id.button_dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                Integer currentCount = counts.get(position);
                if (currentCount > 0) {
                    counts.put(position, currentCount - 1);
                }
                reload();
            }
        });

        convertView.findViewById(R.id.button_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                counts.remove(position);
                items.remove((int) position);
                reload();
            }
        });

        return convertView;
    }

    private void reload() {
        ((OnAdapterInteractionListener) context).onAdapterInteraction();
        this.notifyDataSetChanged();
    }

    public static interface OnAdapterInteractionListener {
        void onAdapterInteraction();
    }

    public Map<Integer, Integer> getCounts() {
        return counts;
    }

    public void setCounts(Map<Integer, Integer> counts) {
        this.counts = counts;
    }

    public List<AbstractItem> getItems() {
        return items;
    }

    public void setItems(List<AbstractItem> items) {
        this.items = items;
    }
}
