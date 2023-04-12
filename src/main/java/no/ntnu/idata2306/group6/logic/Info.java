package no.ntnu.idata2306.group6.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Info {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int infoId;
    @ManyToOne
    private int productID;
    private String longDescription;
    private String description;
    private String shortDescription;
    private String imageURL;
    private String croppedURL;
    private String widgetURL;

    /**
     * Constructor for info class.
     *
     * @param longDescription for describing the product in a detailed way
     * @param description medium description
     * @param shortDescription for describing the product in a short way
     * @param imageURL url for the primary image
     * @param croppedURL url for the cropped image
     * @param widgetURL url for a widget version of the image
     */
    public Info(String longDescription, String description, String shortDescription,
                String imageURL, String croppedURL, String widgetURL) {

        setLongDescription(longDescription);
        setDescription(description);
        setShortDescription(shortDescription);
        setImageURL(imageURL);
        setCroppedURL(croppedURL);
        setWidgetURL(widgetURL);
    }

    public Info() {

    }

    /**
     * Get id of the info object.
     *
     * @return id of the info
     */
    public int getInfoId() {
        return infoId;
    }

    /**
     * Get the product id.
     *
     * @return id of the product it is coupled to
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Set the product id.
     *
     * @param productID id of the product
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Get the long description of this info.
     *
     * @return long description of info
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Set the long description of the info.
     *
     * @param description1 long description
     */
    public void setLongDescription(String description1) {
        this.longDescription = description1;
    }

    /**
     * Get the description.
     *
     * @return the description of this info
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this info.
     *
     * @param description new description for this info
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the short description of this info.
     *
     * @return short description of this info
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Set the short description of the info.
     *
     * @param shortDescription new short description for this info
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Get the basic image url for this info.
     *
     * @return url of the basic image
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     *
     * @param imageURL
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Returns the cropped URL.
     *
     * @return the cropped URL as a String
     */
    public String getCroppedURL() {
        return croppedURL;
    }

    /**
     * Sets the cropped URL.
     *
     * @param croppedURL the new cropped URL as a String
     */
    public void setCroppedURL(String croppedURL) {
        this.croppedURL = croppedURL;
    }

    /**
     * Returns the widget URL.
     *
     * @return the widget URL as a String
     */
    public String getWidgetURL() {
        return widgetURL;
    }

    /**
     * Sets the widget URL.
     *
     * @param widgetURL the new widget URL as a String
     */
    public void setWidgetURL(String widgetURL) {
        this.widgetURL = widgetURL;
    }
}
