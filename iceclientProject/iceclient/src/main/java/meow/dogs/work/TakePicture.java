package meow.dogs.work;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import ice.BaseMessage;
import ice.DataForRecord;
import ice.Photo;

public class TakePicture
{
    public byte[] photodata=null;
    public boolean error=false;
    private Activity myac;
    private DataForRecord.TypeEvent myTE;
    public static void takePictureNP(Context context,Activity act)
    {
        Camera myCamera=openCameraGingerbread();
        if(myCamera!=null)
        {
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getCallback(context,act));
            } catch (IOException e) {
                Toast.makeText(act, "((((((", Toast.LENGTH_LONG).show();
            } finally{
            }
        }else{
            Toast.makeText(act, "myCamera==null", Toast.LENGTH_LONG).show();
        }
    }
    public static void takePictureNP(Activity act)
    {
        Context context=act.getApplicationContext();
        Camera myCamera=openCameraGingerbread();
        if(myCamera!=null)
        {
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getCallback(context,act));
            } catch (IOException e) {
                Toast.makeText(act, "((((((", Toast.LENGTH_LONG).show();
            } finally{
            }
        }else{
            Toast.makeText(act, "myCamera==null", Toast.LENGTH_LONG).show();
        }
    }
    public static void takePictureNPnt(Activity act)
    {
        Context context=act.getApplicationContext();
        Camera myCamera=openCameraGingerbread();
        if(myCamera!=null)
        {
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getCallback(context,act));
            } catch (IOException e) {
                Toast.makeText(act, "((((((", Toast.LENGTH_LONG).show();
            } finally{
            }
        }else{
            Toast.makeText(act, "myCamera==null", Toast.LENGTH_LONG).show();
        }
    }
    public static void takePictureNP(Activity act,DataForRecord.TypeEvent te)
    {
        Context context=act.getApplicationContext();
        Camera myCamera=openCameraGingerbread();
        if(myCamera!=null)
        {
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getCallbackBM(act, te));
            } catch (IOException e) {
                Toast.makeText(act, "((((((", Toast.LENGTH_LONG).show();
            } finally{
            }
        }else{
            Toast.makeText(act, "myCamera==null", Toast.LENGTH_LONG).show();
        }
    }
    public static void takePictureNP(Activity act,DataForRecord dfr)
    {
        Context context=act.getApplicationContext();
        Camera myCamera=openCameraGingerbread();
        if(myCamera!=null)
        {
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getCallbackBM(act,dfr));
            } catch (IOException e) {
                Toast.makeText(act, "((((((", Toast.LENGTH_LONG).show();
            } finally{
            }
        }else{
            Toast.makeText(act, "myCamera==null", Toast.LENGTH_LONG).show();
        }
    }
    private static PictureCallback getCallbackBM(final Activity act, final DataForRecord.TypeEvent te)
    {
        return new PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                String str = Scktmy.
                        //sendwithoutlogin
                                sendobject
                                ((BaseMessage) new Photo(data, te), act);
                if(str==null)
                    return;
                if(str.equals("photook"))
                {
                    Toast.makeText(act, "Отчёт составлен", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(act, "Что-то пошло не так с фото", Toast.LENGTH_LONG).show();
                }
                camera.stopPreview();
                camera.release();
            }
        };
    }
    private static PictureCallback getCallbackBM(final Activity act, final DataForRecord dfr)
    {
        return new PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                String str = Scktmy.sendTWOobject
                                ((BaseMessage)dfr,(BaseMessage) new Photo(data, dfr.getTypeEvent()), act);
                if(str==null)
                    return;
                if(str.equals("photook"))
                {
                    if(Arbeiten.ssarb==Arbeiten.SessionStatus.open)
                    {
                        Arbeiten.closesession();
                        Arbeiten.ssarb= Arbeiten.SessionStatus.close;
                    }
                    if(Arbeiten.ssarb==Arbeiten.SessionStatus.start)
                    {
                        Arbeiten.opensession();
                        Arbeiten.ssarb= Arbeiten.SessionStatus.open;
                    }
                }
                else
                {
                    Toast.makeText(act, "Что-то пошло не так с фото", Toast.LENGTH_LONG).show();
                }
                camera.stopPreview();
                camera.release();
            }
        };
    }
    private static PictureCallback getCallback(final Context context, final Activity act)
    {
        return new PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                Toast.makeText(act, "фото", Toast.LENGTH_LONG).show();
                camera.stopPreview();
                camera.release();
            }
        };
    }
    public void takePictureNoPreview(Context context)
    {
        Camera myCamera=openFrontFacingCameraGingerbread();
        if(myCamera!=null){
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getJpegCallback(context));
            } catch (IOException e) {
                error=true;
            } finally{
            }
        }else{
            error=true;
        }
    }
    public void takePictureNoPreview2(Context context)
    {
        Camera myCamera=openFrontFacingCameraGingerbread();
        if(myCamera!=null){
            try{
                SurfaceView dummy=new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();
                myCamera.takePicture(null, null, getJpegCallback2(context));
            } catch (IOException e) {
                error=true;
            } finally{
            }
        }else{
            error=true;
        }
    }


    private PictureCallback getJpegCallback2(final Context context)
    {
        return new PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                    Toast.makeText(myac, "фото", Toast.LENGTH_LONG).show();
                camera.release();
            }
        };
    }
    private PictureCallback getJpegCallback(final Context context)
    {
        return new PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                /*
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream("/mnt/sdcard/ForIce/Datatest.jpeg");
                    fos.write(data);
                    fos.close();
                    photodata=data;Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                }  catch (IOException e) {
                    error=true;
                }
                */
                //String str = Scktmy.sendwithoutlogin((BaseMessage)new Photo(data),myac);
                String str = Scktmy.
                        //sendwithoutlogin
                                sendobject
                                ((BaseMessage) new Photo(data, myTE), myac);
                if(str==null)
                    return;
                if(str.equals("photook"))
                {

                }
                else
                {
                    Toast.makeText(myac, "Что-то пошло не так с фото", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private Camera openFrontFacingCameraGingerbread()
    {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for ( int camIdx = 0; camIdx < cameraCount; camIdx++ )
        {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  )
            {
                try {
                    cam = Camera.open( camIdx );
                } catch (RuntimeException e) {
                    error=true;
                }
            }
        }
        if(cam==null)
        {
            for ( int camIdx = 0; camIdx < cameraCount; camIdx++ )
            {
                Camera.getCameraInfo( camIdx, cameraInfo );
                if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK  )
                {
                    try {
                        cam = Camera.open( camIdx );
                    } catch (RuntimeException e) {
                        error=true;
                    }
                }
            }
        }
        return cam;
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static Camera openCameraGingerbread()
    {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for ( int camIdx = 0; camIdx < cameraCount; camIdx++ )
        {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  )
            {
                try {
                    cam = Camera.open( camIdx );
                } catch (RuntimeException e) {
                }
            }
        }
        if(cam==null)
        {
            for ( int camIdx = 0; camIdx < cameraCount; camIdx++ )
            {
                Camera.getCameraInfo( camIdx, cameraInfo );
                if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK  )
                {
                    try {
                        cam = Camera.open( camIdx );
                    } catch (RuntimeException e) {
                    }
                }
            }
        }
        return cam;
    }

    public TakePicture(Activity act,DataForRecord.TypeEvent te)
    {
        myac=act;
        myTE=te;
        takePictureNoPreview(act.getApplicationContext());
    }
    public TakePicture(Activity act)
    {
        myac=act;
        takePictureNoPreview2(act.getApplicationContext());
    }
}
