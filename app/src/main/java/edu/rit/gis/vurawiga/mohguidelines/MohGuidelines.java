package edu.rit.gis.vurawiga.mohguidelines;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class MohGuidelines {

    private static String title;
    private static String imageResource;

    MohGuidelines(String title, String imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageResource() {
        return this.imageResource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
