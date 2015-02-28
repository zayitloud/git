package chess.piece;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Bishop unit tests
 * @author Willie
 *
 */
public class TestBishop  extends TestCase{

	private Logger log= LoggerFactory.getLogger(Bishop.class);
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public final void testGetCaptureSlotsPositionsBorder1() {
		Bishop bishop = new Bishop();
		List<Coordinate> list = bishop.getCaptureSlotsPositions(new Coordinate(1, 1), new Coordinate(7,7));
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
		Bishop bishop = new Bishop();
		List<Coordinate> list = bishop.getCaptureSlotsPositions(new Coordinate(7, 1), new Coordinate(7,7));
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
		Bishop bishop = new Bishop();
		List<Coordinate> list = bishop.getCaptureSlotsPositions(new Coordinate(7, 7), new Coordinate(7,7));
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
		Bishop bishop = new Bishop();
		List<Coordinate> list = bishop.getCaptureSlotsPositions(new Coordinate(1, 7), new Coordinate(7,7));
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
		Bishop bishop = new Bishop();
		List<Coordinate> list = bishop.getCaptureSlotsPositions(new Coordinate(4, 4), new Coordinate(7,7));
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
		Bishop bishop = new Bishop();
		List<Coordinate> list = bishop.getCaptureSlotsPositions(new Coordinate(2, 4), new Coordinate(7,7));
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
