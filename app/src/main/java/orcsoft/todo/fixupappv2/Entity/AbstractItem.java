package orcsoft.todo.fixupappv2.Entity;

import java.util.List;

public abstract class AbstractItem {
    protected Integer id;
    protected String title;
    protected Integer cost;
    protected String unit_title;
    protected String unit_title_short;
    protected List<String> item_list;
    //    --
    protected Integer count;
    protected Integer total;

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getUnit_title() {
        return unit_title;
    }

    public void setUnit_title(String unit_title) {
        this.unit_title = unit_title;
    }

    public String getUnit_title_short() {
        return unit_title_short;
    }

    public void setUnit_title_short(String unit_title_short) {
        this.unit_title_short = unit_title_short;
    }
}
