package com.bskyb.internettv.parental_control_service_impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.bskyb.internettv.parental_control_service.ParentalControlService;
import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;
import com.bskyb.internettv.parental_control_service_impl.ParentalControlServiceImpl;;
 
 

 

@RunWith(JUnit4.class)
public class ParentalControlServiceImplTest {

	 MovieService movieService;
	    ParentalControlService pControlService;

    @Before
    public void Setup() throws TechnicalFailureException, TitleNotFoundException {
        movieService = mock(MovieService.class);
         
    }
    
    @Test(expected = TechnicalFailureException.class)
    public void nullMovieServiceShouldThrowTechnicalFailureException() throws Exception{
        new ParentalControlServiceImpl(null);
    }

    @Test(expected = TechnicalFailureException.class)
    public void invalidLevelShpuldThrowTechnicalFailureException() throws Exception{
        final String movieId = "1";
        final String movieParentalControlLevel = "PG";

        when(movieService.getParentalControlLevel(movieId)).thenReturn(movieParentalControlLevel);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        target.canWatchMovie(movieId, "fish");
    }
    
    @Test(expected = TitleNotFoundException.class)
    public void unknownTitleShouldThrowTitleNotFoundException() throws Exception{
        final String movieId = "2";
        final String customerParentalControlLevel = "PG";

        when(movieService.getParentalControlLevel(anyString())).thenThrow(TitleNotFoundException.class);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        target.canWatchMovie(movieId, customerParentalControlLevel);
    }

    @Test(expected = TechnicalFailureException.class)
    public void movieServiceThrowsRuntimeException() throws Exception{
        final String movieId = "2";
        final String customerParentalControlLevel = "PG";

        when(movieService.getParentalControlLevel(anyString())).thenThrow(RuntimeException.class);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        target.canWatchMovie(movieId, customerParentalControlLevel);
    }
    
    @Test(expected = TechnicalFailureException.class)
    public void invalidReturnFromMovieServiceShouldThrowTechnicalFault() throws Exception{
        final String movieId = "1";
        final String customerParentalControlLevel = "PG";
        final String movieParentalControlLevel = "fish";

        when(movieService.getParentalControlLevel(movieId)).thenReturn(movieParentalControlLevel);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        target.canWatchMovie(movieId, customerParentalControlLevel);
    }

    @Test(expected = TechnicalFailureException.class)
    public void nullReturnFromMovieServiceShouldThrowTechnicalFault() throws Exception{
        final String movieId = "1";
        final String customerParentalControlLevel = "PG";
        final String movieParentalControlLevel = null;

        when(movieService.getParentalControlLevel(movieId)).thenReturn(null);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        target.canWatchMovie(movieId, customerParentalControlLevel);
    }

    @Test
    public void sameLevelShouldReturnTrue() throws Exception{
        final String movieId = "1";
        final String customerParentalControlLevel = "PG";
        final String movieParentalControlLevel = "PG";

        when(movieService.getParentalControlLevel(movieId)).thenReturn(movieParentalControlLevel);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        assertTrue(target.canWatchMovie(movieId, customerParentalControlLevel));
    }

    @Test
    public void higherMovieShouldReturnFalse() throws Exception{
        final String movieId = "1";
        final String customerParentalControlLevel = "U";
        final String movieParentalControlLevel = "PG";

        when(movieService.getParentalControlLevel(movieId)).thenReturn(movieParentalControlLevel);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        assertFalse(target.canWatchMovie(movieId, customerParentalControlLevel));
    }

    @Test
    public void lowerMovieShouldReturnTrue() throws Exception{
        final String movieId = "1";
        final String customerParentalControlLevel = "18";
        final String movieParentalControlLevel = "15";

        when(movieService.getParentalControlLevel(movieId)).thenReturn(movieParentalControlLevel);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        assertTrue(target.canWatchMovie(movieId, customerParentalControlLevel));
    }

    @Test(expected = TechnicalFailureException.class)
    public void movieLevelShouldBeCaseSensitive() throws Exception{
        final String movieId = "1";
        final String customerParentalControlLevel = "u";
        final String movieParentalControlLevel = "15";

        when(movieService.getParentalControlLevel(movieId)).thenReturn(movieParentalControlLevel);

        ParentalControlService target = new ParentalControlServiceImpl(movieService);
        target.canWatchMovie(movieId, customerParentalControlLevel);
    }
}
