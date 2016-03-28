package orcsoft.todo.fixupappv2.Entity;

import java.util.List;

public class Product extends AbstractItem{

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", cost=" + cost +
            ", unit_title='" + unit_title + '\'' +
            ", unit_title_short='" + unit_title_short + '\'' +
            ", count=" + count +
            ", total=" + total +
            ", item_list=" + item_list +
            '}';
    }
}
