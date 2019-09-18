__kernel void
sepia_kernel(__global const int * input, __global int * result) 
{ 
    	int i = get_global_id(0);


	int pixel = input[i]; 
			
	int red = pixel & 0x00ff0000;    
	red = red >> 16;
	
	int green = pixel & 0x0000ff00;
	green = green >> 8;

	int blue = pixel & 0x000000ff;
		    
	int average = (int) ((red + blue + green) / 3);
			    
	red = average + (20 * 2);
	blue = average - 30;
	green = average + 20;
			    
	if (red > 255) {
		red = 255;
	}
			 
	if (green > 255) {
	 	green = 255;
	}
			    
	if (blue < 0) {
	  	blue = 0;
	}

	int newPixel = (red << 16) | (green << 8) | blue | (0xff000000 & pixel);
        
	result[i] = newPixel;
}