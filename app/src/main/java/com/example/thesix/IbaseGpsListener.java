package com.example.thesix;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
/**
 * Interface for receiving GPS location updates and status changes.
 * Extends LocationListener and GpsStatus.Listener interfaces.
 */
public interface IbaseGpsListener extends LocationListener, GpsStatus.Listener {

    /** Location Updates when changed
     * @param location the updated location
     */
    public void onLocationChanged(Location location);

    /** On provider Disabled
     * @param provider the name of the location provider
     */
    public void onProviderDisabled(String provider);

    /** On provider Enabled
     * @param provider the name of the location provider
     */
    public void onProviderEnabled(String provider);

    /** Status change of location
     * @param provider the name of the location provider
     * @param status status of provider
     * @param extras bundle of extras
     */
    public void onStatusChanged(String provider, int status , Bundle extras);

    /** On GPS status changed
     * @param event event number for this notification
     */
    public void onGpsStatusChanged(int event);
}
