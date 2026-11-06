import type { NextConfig } from "next";
import { basePath } from "@/lib/links";

const nextConfig: NextConfig = {
  output: "standalone",
  basePath,
};

export default nextConfig;
