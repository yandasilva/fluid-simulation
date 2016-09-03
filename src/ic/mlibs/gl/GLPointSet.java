package ic.mlibs.gl;

//Static LWJGL
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

//LWJGL
import org.lwjgl.util.glu.Sphere;

//Imports
import ic.mlibs.structures.Point3D;
import ic.mlibs.structures.PointSet;
import ic.mlibs.util.ColorMap;

public class GLPointSet extends PointSet implements GLObject{
  
    private final ColorMap map = new ColorMap();

    @Override
    public void render(String cmap, String rmode, double pmin, double pmax) {
        //Sets color map
        map.setColorMap(cmap);
        //Sets color map max and min values
        map.setMapExtrems(pmin, pmax);

        //Sets render mode
        if (rmode.contains("Points")) {
            drawVerts();

        } else if (rmode.contains("Spheres")) {
            drawSpheres();
        }
                
        //Changes color map max and min values
        map.setMapExtrems(0, 255);
        //Draws color palette
        map.drawMap(-1.25f,-1.25f,0.1f,2.5f);
    }

    //Draws as points
    private void drawVerts() {

        glPointSize(8);
        glBegin(GL_POINTS);
        {
            for (int i = 0; i < getNvert(); i++) {
                Point3D v = getG(i);

                //Vertex color
                map.setGLColor((Double) v.getObj(), (byte) 255, true);
                //Vertex normal
                glNormal3d(v.getNrmX(), v.getNrmY(), v.getNrmZ());
                //Vertex position
                glVertex3d(v.getPosX(), v.getPosY(), v.getPosZ());
            }
        }
        glEnd();
        glPointSize(1);
    }

    //Draw as spheres
    private void drawSpheres() {

        //Creates sphere
        Sphere s = new Sphere();
        //Render setup
        s.setDrawStyle(GLU_FILL);      
        s.setNormals(GLU_SMOOTH);
        
        for (int i = 0; i < getNvert(); i++) {
            Point3D v = getG(i);
            
            //Positions sphere
            glPushMatrix();
            glTranslated(v.getPosX(), v.getPosY(), v.getPosZ());

            //Vertex color
            map.setGLColor((Double) v.getObj(), (byte) 255, true);
            s.draw( 0.03f, 5, 5);
            
            //Returns matrix
            glPopMatrix();
          }
     }
}
