import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class KML {

	private Kml kml;

	@Test
	public void parseKml() {
		String src = "u2.kml";
		InputStream is = getClass().getClassLoader().getResourceAsStream(src);
		Assert.assertNotNull(is);
		kml = Kml.unmarshal(is);
		Feature feature = kml.getFeature();
		parseFeature(feature);
	}

	private Geometry getGeometry(Kml kml) {
		if (kml != null) {
			Feature feature = kml.getFeature();
			if (feature instanceof Document) {
				Document document = (Document) feature;
				List<Feature> featureList = document.getFeature();
				for (Feature documentFeature : featureList) {
					if (documentFeature instanceof Placemark) {
						Placemark placemark = (Placemark) documentFeature;
						return placemark.getGeometry();
					}
				}
			}
		}
		return null;
	}
	
	
	private void parseFeature(Feature feature) {
		if (feature != null) {
			if (feature instanceof Document) {
				Document document = (Document) feature;
				List<Feature> featureList = document.getFeature();
				for (Feature documentFeature : featureList) {
					System.out.println("Feature: "+documentFeature.getName());
					if (documentFeature instanceof Placemark) {
						Placemark placemark = (Placemark) documentFeature;
						Geometry geometry = placemark.getGeometry();
						parseGeometry(geometry);
					}
				}
			}
		}
	}

	private void parseGeometry(Geometry geometry) {
		if (geometry != null) {
			//Path
			if (geometry instanceof LineString) {
				LineString lineString = (LineString) geometry;
				List<Coordinate> coordinates = lineString.getCoordinates();
				if (!coordinates.isEmpty()) {
					printCoordinates(coordinates);
				}
			}
			//Polygon
			if (geometry instanceof Polygon) {
				Boundary outerBoundaryIs = (Boundary) ((Polygon) geometry).getOuterBoundaryIs();
				if (outerBoundaryIs != null) {
					LinearRing linearRing = outerBoundaryIs.getLinearRing();
					if (linearRing != null) {
						List<Coordinate> coordinates = linearRing.getCoordinates();
						if (!coordinates.isEmpty()) {
							printCoordinates(coordinates);

						}
					}

				}
			}else {
				System.out.println("KML type not suported");
			}
		}

	}

	private void printCoordinates(List<Coordinate> coordinates) {
		for (Coordinate coordinate : coordinates) {
			System.out.println("Longitude: " + coordinate.getLongitude());
			System.out.println("Latitude : " + coordinate.getLatitude());
		}
	}

	/**
	 * @return the kml
	 */
	public Kml getKml() {
		return kml;
	}

	/**
	 * @param kmlFile 
	 */
	public void setKmlObject(String kmlFile) {
		kml = Kml.unmarshal(getClass().getClassLoader().getResourceAsStream(kmlFile));
	}

	
}
