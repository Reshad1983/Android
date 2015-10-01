package com.example.reshad.assignment_1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reshad on 2015-09-10.
 */
public class BeerDetails implements Parcelable {
    private int image_id;
    private String beer_name;
    private String brewery;
    private String rating;
    private String abv_value;
    private String styling;
    private String review;

    public BeerDetails(int image_id, String beer_name, String breewry,
                       String rating, String abv_value, String styling, String review)

    {
        this.abv_value = abv_value;
        this.image_id = image_id;
        this.beer_name = beer_name;
        this.rating = rating;
        this.beer_name = beer_name;
        this.brewery = breewry;
        this.styling = styling;
        this.review = review;
    }

    public BeerDetails(Parcel parcel)
    {
        image_id = parcel.readInt();
        beer_name = parcel.readString();
        brewery = parcel.readString();
        rating = parcel.readString();
        abv_value = parcel.readString();
        styling = parcel.readString();
        review = parcel.readString();
    }
    public String getBeer_name()
    {
        return beer_name;
    }
    public int getImage_id(){return image_id;}
    public String getBeerName(){return beer_name;}
    public String getBrewery(){return brewery;}
    public String getRating(){return rating;}
    public String getAbv_value(){return abv_value;}
    public String getStyling(){return styling;}
    public String getReview(){return review;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeInt(image_id);
        parcel.writeString(beer_name);
        parcel.writeString(brewery);
        parcel.writeString(rating);
        parcel.writeString(abv_value);
        parcel.writeString(styling);
        parcel.writeString(review);
    }

    public static Creator<BeerDetails> CREATOR = new Creator<BeerDetails>() {
        @Override
        public BeerDetails createFromParcel(Parcel source) {
            return new BeerDetails(source);
        }

        @Override
        public BeerDetails[] newArray(int size) {
            return new BeerDetails[size];
        }
    };

}
