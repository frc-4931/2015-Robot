/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.utils;

/**
 * 
 */
public interface Lifecycle {

    /**
     * Prepare this component for use.
     */
    void startup();

    /**
     * Shutdown this component and release any claimed resources.
     */
    void shutdown();
    
}
