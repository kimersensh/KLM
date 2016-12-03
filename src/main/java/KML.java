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

	@Test
	public void parseKml() {
		String src = "u1.kml";
		InputStream is = getClass().getClassLoader().getResourceAsStream(src);
		Assert.assertNotNull(is);
		Kml kml = Kml.unmarshal(is);
		Feature feature = kml.getFeature();
		parseFeature(feature);
	}

	private void parseFeature(Feature feature) {
		if (feature != null) {
			if (feature instanceof Document) {
				Document document = (Document) feature;
				List<Feature> featureList = document.getFeature();
				for (Feature documentFeature : featureList) {
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
			if (geometry instanceof LineString) {
				LineString lineString = (LineString) geometry;
				List<Coordinate> coordinates = lineString.getCoordinates();
				if (coordinates != null) {
					for (Coordinate coordinate : coordinates) {
						parseCoordinate(coordinate);
					}
				}
			}
			if(geometry instanceof Polygon) {
	            Polygon polygon = (Polygon) geometry;
	            Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
	            if(outerBoundaryIs != null) {
	                LinearRing linearRing = outerBoundaryIs.getLinearRing();
	                if(linearRing != null) {
	                    List<Coordinate> coordinates = linearRing.getCoordinates();
	                    if(coordinates != null) {
	                        for(Coordinate coordinate : coordinates) {
	                            parseCoordinate(coordinate);
	                        }
	                    }
	                }
	            }
	        }
		}
	}

	private void parseCoordinate(Coordinate coordinate) {
		if (coordinate != null) {
			System.out.println("Longitude: " + coordinate.getLongitude());
			System.out.println("Latitude : " + coordinate.getLatitude());
			//System.out.println("Altitude : " + coordinate.getAltitude());
			System.out.println("");
		}
	}

	// @Test
	// public void parseKml() {
	// final Kml kml =
	// Kml.unmarshal(getClass().getClassLoader().getResourceAsStream("u.kml"));
	// final Placemark placemark = (Placemark) kml.getFeature();
	// Point point = (Point) placemark.getGeometry();
	// List<Coordinate> coordinates = point.getCoordinates();
	// for (Coordinate coordinate : coordinates) {
	// System.out.println(coordinate.getLatitude());
	// System.out.println(coordinate.getLongitude());
	// System.out.println(coordinate.getAltitude());
	// }
	// }

}
