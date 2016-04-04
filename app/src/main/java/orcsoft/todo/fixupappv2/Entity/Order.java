package orcsoft.todo.fixupappv2.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@DatabaseTable(tableName = "orders")
public class Order implements Parcelable {
    public static DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat SDF_WITH_HOURS = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    public static DateFormat SDF_DATETIME_WORKS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static DateFormat SDF_DATE_TIME = new SimpleDateFormat("dd MMMM hh:mm");

    public static enum Category {FREE, ACTIVE, DONE, ARCHIVE};

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String client_firstname;

    @DatabaseField
    private String client_lastname;

    @DatabaseField
    private String address;

    @DatabaseField
    private String date_wish;

    @DatabaseField
    private String interval_title;

    @DatabaseField
    private String datetime_works;

    @DatabaseField
    private String comment;

    @DatabaseField
    private String cost;

    @DatabaseField
    private Category category;

    @DatabaseField
    private Double longitude;

    @DatabaseField
    private Double latitude;

    public Order() {
    }

    public Order(Parcel source) {
        if (source != null) {
            String[] array = new String[9];
            source.readStringArray(array);
            client_firstname = array[0];
            client_lastname = array[1];
            address = array[2];
            date_wish = array[3];
            interval_title = array[4];
            datetime_works = array[5];
            comment = array[6];
            cost = array[7];
            if (array[8] != null) {
                category = Category.valueOf(array[8]);
            }
            id = source.readInt();
            latitude = source.readDouble();
            longitude = source.readDouble();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClient_firstname() {
        return client_firstname;
    }

    public void setClient_firstname(String client_firstname) {
        this.client_firstname = client_firstname;
    }

    public String getClient_lastname() {
        return client_lastname;
    }

    public void setClient_lastname(String client_lastname) {
        this.client_lastname = client_lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate_wish() {
        return date_wish;
    }

    public void setDate_wish(String date_wish) {
        this.date_wish = date_wish;
    }

    public String getInterval_title() {
        return interval_title;
    }

    public void setInterval_title(String interval_title) {
        this.interval_title = interval_title;
    }

    public String getDatetime_works() {
        return datetime_works;
    }

    public void setDatetime_works(String datetime_works) {
        this.datetime_works = datetime_works;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", date_wish='" + date_wish + '\'' +
                ", client_firstname='" + client_firstname + '\'' +
                ", client_lastname='" + client_lastname + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                client_firstname,
                client_lastname,
                address,
                date_wish,
                interval_title,
                datetime_works,
                comment,
                cost,
                category.name()});
        dest.writeInt(id);
        if(latitude == null){
            latitude = 0.0;
        }
        dest.writeDouble(latitude);
        if(longitude == null){
            longitude = 0.0;
        }
        dest.writeDouble(longitude);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
