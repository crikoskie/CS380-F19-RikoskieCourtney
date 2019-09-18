__kernel void
grayscale_kernel(__global const int * input, __global int * result) 
{ 
    	int i = get_global_id(0);


	int pixel = input[i]; 
			
	int red = pixel & 0x00ff0000;    
	red = red >> 16;
	
	int green = pixel & 0x0000ff00;
	green = green >> 8;

	int blue = pixel & 0x000000ff;
		    
	int gray = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

	int newPixel = (gray << 16) | (gray << 8) | gray | (0xff000000 & pixel);
        
	result[i] = newPixel;
}