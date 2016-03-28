package orcsoft.todo.fixupappv2.Entity;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class Service extends AbstractItem {

    @Override
    public String getUnit_title() {
        return StringUtils.EMPTY;
    }

    @Override
    public void setUnit_title(String unit_title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUnit_title_short() {
        return StringUtils.EMPTY;
    }

    @Override
    public void setUnit_title_short(String unit_title_short) {
        throw new UnsupportedOperationException();
    }

    public List<String> getItem_list() {
        return Collections.EMPTY_LIST;
    }

    public void setItem_list(List<String> item_list) {
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                ", count=" + count +
                ", total=" + total +
                '}';
    }
}
