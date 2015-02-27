package chess.piece;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some test cases for the 
 * @author Willie
 *
 */
public class TestKnight extends TestCase{

	private Logger log= LoggerFactory.getLogger(TestKnight.class);
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public final void testGetCaptureSlotsPositionsBorder1() {
		Knight knight = new Knight();
		List<Coordinate> list = knight.getCaptureSlotsPositions(new Coordinate(1, 1), new Coordinate(7,7));
		log.info("Test position (1,1) size 7x7");
		if(list.size()>0)
		{
			for(Coordinate coordinate: list)
			{
				log.info("(" + coordinate.getX()+"," + coordinate.getY()+")");
			}
			assertTrue(list.size()>0);
		}else{
			fail("Not yet implemented");
		}
	}

	@Test
	public final void testGetCaptureSlotsPositionsBorder2() {
		Knight knight = new Knight();
		List<Coordinate> list = knight.getCaptureSlotsPositions(new Coordinate(7, 1), new Coordinate(7,7));
		log.info("Test position (7,1) size 7x7");
		if(list.size()>0)
		{
			for(Coordinate coordinate: list)
			{
				log.info("(" + coordinate.getX()+"," + coordinate.getY()+")");
			}
			assertTrue(list.size()>0);
		}else{
			fail("Not yet implemented");
		}
	}

	@Test
	public final void testGetCaptureSlotsPositionsBorder3() {
		Knight knight = new Knight();
		List<Coordinate> list = knight.getCaptureSlotsPositions(new Coordinate(7, 7), new Coordinate(7,7));
		log.info("Test position (7,7) size 7x7");
		if(list.size()>0)
		{
			for(Coordinate coordinate: list)
			{
				log.info("(" + coordinate.getX()+"," + coordinate.getY()+")");
			}
			assertTrue(list.size()>0);
		}else{
			fail("Not yet implemented");
		}
	}
	
	@Test
	public final void testGetCaptureSlotsPositionsBorder4() {
		Knight knight = new Knight();
		List<Coordinate> list = knight.getCaptureSlotsPositions(new Coordinate(1, 7), new Coordinate(7,7));
		log.info("Test position (1,7) size 7x7");
		if(list.size()>0)
		{
			for(Coordinate coordinate: list)
			{
				log.info("(" + coordinate.getX()+"," + coordinate.getY()+")");
			}
			assertTrue(list.size()>0);
		}else{
			fail("Not yet implemented");
		}
	}
	
	@Test
	public final void testGetCaptureSlotsPositions() {
		Knight knight = new Knight();
		List<Coordinate> list = knight.getCaptureSlotsPositions(new Coordinate(4, 4), new Coordinate(7,7));
		log.info("Test position (4,4) size 7x7");
		if(list.size()>0)
		{
			for(Coordinate coordinate: list)
			{
				log.info("(" + coordinate.getX()+"," + coordinate.getY()+")");
			}
			assertTrue(list.size()>0);
		}else{
			fail("Not yet implemented");
		}
	}

	@Test
	public final void testGetCaptureSlotsPositionsCenter() {
		Knight knight = new Knight();
		List<Coordinate> list = knight.getCaptureSlotsPositions(new Coordinate(2, 4), new Coordinate(7,7));
		log.info("Test position (2,4) size 7x7");
		if(list.size()>0)
		{
			for(Coordinate coordinate: list)
			{
				log.info("(" + coordinate.getX()+"," + coordinate.getY()+")");
			}
			assertTrue(list.size()>0);
		}else{
			fail("Not yet implemented");
		}
	}

}
