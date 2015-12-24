package edu.rit.gis.vurawiga.tip_of_day;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import edu.rit.gis.vurawiga.R;

/**
 * Created by siddeshpillai on 10/17/15.
 */
public class MapsActivity extends Activity {

    MapView mMapView = null;
    LocationDisplayManager lDisplayManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        ArcGISRuntime.setClientId("id72YurCdbXivGgU");

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setEsriLogoVisible(true);
        mMapView.enableWrapAround(true);

        // Create GraphicsLayer
        GraphicsLayer gLayer = new GraphicsLayer();

        // Add empty GraphicsLayer
        mMapView.addLayer(gLayer);

        PictureMarkerSymbol symbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ic_action_action_home));
//        SimpleRenderer renderer = new SimpleRenderer(symbol);
//        renderer.setLabel("King Faisal Hospital");

        Point mapPoint = new Point(-1.9439147, 30.093036, SpatialReference.WKID_WGS84);
        Graphic graphic = new Graphic(mapPoint, symbol);
//        gLayer.setRenderer(renderer);
        gLayer.addGraphic(graphic);
        mMapView.addLayer(gLayer);


        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {


                    lDisplayManager = mMapView.getLocationDisplayManager();
                    lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

                    lDisplayManager.setLocationListener(new LocationListener() {

                        boolean locationChanged = false;

                        // Zooms to the current location when first GPS fix arrives.
                        @Override
                        public void onLocationChanged(Location loc) {
                            if (!locationChanged) {

                                locationChanged = true;


                                double locy = loc.getLatitude();
                                double locx = loc.getLongitude();
                                Point wgspoint = new Point(locx, locy);
                                Point mapPoint = (Point) GeometryEngine
                                        .project(wgspoint,
                                                SpatialReference.create(4326),
                                                mMapView.getSpatialReference());

                                Unit mapUnit = mMapView.getSpatialReference()
                                        .getUnit();
                                double zoomWidth = Unit.convertUnits(
                                        5,
                                        Unit.create(LinearUnit.Code.MILE_US),
                                        mapUnit);
                                Envelope zoomExtent = new Envelope(mapPoint,
                                        zoomWidth, zoomWidth);
                                mMapView.setExtent(zoomExtent);
                            }
                        }

                        @Override
                        public void onProviderDisabled(String arg0) {
                        }

                        @Override
                        public void onProviderEnabled(String arg0) {
                        }

                        @Override
                        public void onStatusChanged(String arg0, int arg1,
                                                    Bundle arg2) {

                        }
                    });  // Actionlistener

                    lDisplayManager.start();
                }
            }
        });
    }

}