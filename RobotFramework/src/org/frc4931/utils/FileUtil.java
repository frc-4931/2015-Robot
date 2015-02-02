/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * File utilities.
 */
public final class FileUtil {
    
    private static DateTimeFormatter MINUTE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-ddTHHmm");
    
    public static Path namedWithTimestamp( LocalTime timestamp, String prefix, String suffix ) {
        return namedWithTimestamp(timestamp,Paths.get("."), prefix, suffix);
    }
    
    public static Path namedWithTimestamp( LocalTime timestamp, Path directory, String prefix, String suffix ) {
        assert directory != null;
        StringBuilder filename = new StringBuilder();
        if ( prefix != null ) filename.append(prefix);
        filename.append(timestamp.format(MINUTE_FORMATTER));
        if ( suffix != null ) filename.append(suffix);
        return directory.resolve(filename.toString());
    }

    private FileUtil() {
    }

}
