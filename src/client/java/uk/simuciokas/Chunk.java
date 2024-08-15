package uk.simuciokas;

public class Chunk {
    public int x;
    public int z;

	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chunk chunk = (Chunk) o;
		return x == chunk.x && z == chunk.z;
	}
}
