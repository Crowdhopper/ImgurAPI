package moe.crowdhopper.imgurapi.Models;

//Exists purely to allow GalleryImages and GalleryAlbums to be in the same list.
public interface Gallery {
	public abstract boolean isAlbum();
}
