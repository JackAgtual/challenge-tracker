import React from "react";

export function CenterContainer({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex justify-center h-full items-center">
      <div className="w-full max-w-3xl">{children}</div>
    </div>
  );
}
