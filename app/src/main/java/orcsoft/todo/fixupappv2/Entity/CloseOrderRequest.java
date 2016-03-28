package orcsoft.todo.fixupappv2.Entity;

import java.util.List;

public class CloseOrderRequest {
    private int[] product_list;
    private int[] service_list;

    public CloseOrderRequest(List<Product> products, List<Service> services) {
        int pSize = -1;
        for(Product pi : products){
            if(pSize < pi.getId()){
                pSize = pi.getId();
            }
        }

        if(pSize >= 0){
            product_list = new int[pSize + 1];
            for (int i = 0; i < pSize; ++i) {
                product_list[i] = 0;
            }
            for (Product pi : products){
                product_list[pi.getId()] = pi.count;
            }
        } else {
            product_list = new int[0];
        }

        int sSize = -1;
        for(Service si :services){
            if(sSize < si.getId()){
                sSize = si.getId();
            }
        }

        if(sSize >= 0 ){
            service_list = new int[sSize + 1];
            for (int i = 0; i < sSize; ++i) {
                service_list[i] = 0;
            }
            for (Service si : services){
                service_list[si.getId()] = si.count;
            }
        } else {
            service_list = new int[0];
        }
    }

    public int[] getProduct_list() {
        return product_list;
    }

    public int[] getService_list() {
        return service_list;
    }
}
