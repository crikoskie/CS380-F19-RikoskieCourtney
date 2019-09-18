package kings.image;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

public class ParallelSetUp {
	final int platformIndex = 0;
	final long deviceType = CL.CL_DEVICE_TYPE_ALL;

	private int deviceIndex;
	private cl_platform_id platform;
	private int[] resultImage;
	
	private cl_kernel kernel;
	private cl_program program;

	public ParallelSetUp() {
		CL.setExceptionsEnabled(true);

		deviceIndex = 0;
		platform = getPlatformID();
		
		kernel = null;
		program = null;
		
		resultImage = null;
	}

	public void runAlgorithm(String algorithm, int[] image) {
		resultImage = new int[image.length];
		
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);

		cl_device_id device = getDeviceID();

		cl_context context = CL.clCreateContext(contextProperties, 1, new cl_device_id[] { device }, null, null, null);

		cl_command_queue commandQueue = CL.clCreateCommandQueue(context, device, 0, null);

		Pointer ptrImage = Pointer.to(image);

		cl_mem memImage = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
				Sizeof.cl_int * image.length, ptrImage, null);

		cl_mem memResult = CL.clCreateBuffer(context, CL.CL_MEM_READ_WRITE, Sizeof.cl_float * image.length, null, null);

		decideAlgorithm(algorithm, context);
		
		executeKernel(kernel, memImage, memResult, commandQueue, image);
		
		CL.clEnqueueReadBuffer(commandQueue, memResult, CL.CL_TRUE, 0, Sizeof.cl_float * image.length, Pointer.to(resultImage), 0,
				null, null);

		CL.clReleaseKernel(kernel);
		CL.clReleaseProgram(program);
		CL.clReleaseMemObject(memImage);
		CL.clReleaseMemObject(memResult);
		CL.clReleaseCommandQueue(commandQueue);
		CL.clReleaseContext(context);
	}

	private cl_platform_id getPlatformID() {
		int[] numPlatformsArray = new int[1];
		CL.clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
		CL.clGetPlatformIDs(platforms.length, platforms, null);

		return platforms[platformIndex];
	}

	private cl_device_id getDeviceID() {
		cl_device_id[] devices = getAllDevices();

		return devices[deviceIndex];
	}

	public void setDeviceID(int index) {
		deviceIndex = index;
	}

	private void grayscale(cl_context context) {
		String source = readFile("algorithms/grayscale_kernel.cl");
		
		program = CL.clCreateProgramWithSource(context, 1, new String[] { source }, null, null);
		CL.clBuildProgram(program, 0, null, null, null, null);
		
		kernel = CL.clCreateKernel(program, "grayscale_kernel", null);
	}
	
	private void sepia(cl_context context) {
		String source = readFile("algorithms/sepia_kernel.cl");
		
		program = CL.clCreateProgramWithSource(context, 1, new String[] { source }, null, null);
		CL.clBuildProgram(program, 0, null, null, null, null);
		
		kernel = CL.clCreateKernel(program, "sepia_kernel", null);
	}

	private void executeKernel(cl_kernel kernel, cl_mem memImage, cl_mem memResult, cl_command_queue commandQueue, int[] image) {
		CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memImage));
		CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memResult));

		long[] globalWorkSize = new long[] { image.length };

		CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, globalWorkSize, null, 0, null, null);  
	}

	public String[] getDeviceNames() {
		cl_device_id[] devices = getAllDevices();

		String[] names = new String[devices.length];

		for (int index = 0; index < names.length; index += 1) {
			cl_device_id id = devices[index];

			// Obtain the length of the string that will be queried
			long[] size = new long[1];
			CL.clGetDeviceInfo(id, CL.CL_DEVICE_NAME, 0, null, size);

			// Create a buffer of the appropriate length and fill it
			byte[] buffer = new byte[(int) size[0]];
			CL.clGetDeviceInfo(id, CL.CL_DEVICE_NAME, buffer.length, Pointer.to(buffer), null);

			// Create a String from the buffer (excluding trailing ’\0’
			String deviceName = new String(buffer, 0, buffer.length - 1);

			names[index] = deviceName;
		}

		return names;
	}

	private cl_device_id[] getAllDevices() {
		// Returns the number of available devices associated with platform
		int[] numDevicesArray = new int[1];
		CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
		int numDevices = numDevicesArray[0];

		// Get the list of device ids
		cl_device_id[] devices = new cl_device_id[numDevices];
		CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);

		return devices;
	}

	public int getGPUIndex() {
		boolean found = false;
		int index = 0;
		int result = 0;
		cl_device_id[] devices = getAllDevices();

		while (!found && index < devices.length) {
			cl_device_id current = devices[index];

			// Obtain the length of the string that will be queried
			long[] size = new long[1];
			CL.clGetDeviceInfo(current, CL.CL_DEVICE_TYPE, 0, null, size);

			// Create a buffer of the appropriate length and f i l l it
			byte[] buffer = new byte[(int) size[0]];
			CL.clGetDeviceInfo(current, CL.CL_DEVICE_TYPE, buffer.length, Pointer.to(buffer), null);

			int type = (int) buffer[0];

			if (type == CL.CL_DEVICE_TYPE_GPU) {
				found = true;
				result = index;
			}
		}

		return result;
	}

	private void decideAlgorithm(String algorithm, cl_context context) {		
		switch (algorithm) {
			case "Grayscale":
				grayscale(context);
				break;
			case "Sepia":
				sepia(context);
				break;
		}
	}
	
	private String readFile(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

	public int[] getResult() {
		return resultImage;
	}
}
