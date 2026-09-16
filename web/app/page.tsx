import LoginButton from "@/components/LoginButton";
import { auth0 } from "@/lib/auth0";
import { redirect } from "next/navigation";

export default async function Home() {
  const session = await auth0.getSession();

  if (session) {
    redirect("/dashboard");
  }

  return (
    <main className="min-h-screen bg-[#efefef] flex flex-col items-center justify-center gap-4 px-6 py-12">
      <div className="bg-white rounded-[28px] shadow-[0_4px_32px_rgba(0,0,0,0.08)] px-12 py-14 flex flex-col items-center gap-4 w-[360px]">
        <h1 className="text-[17px] font-bold text-gray-900 tracking-tight">
          Welcome to Challenge Tracker
        </h1>
        <p className="text-[13px] text-gray-400 text-center leading-relaxed -mt-2">
          Get started by logging in to your account
        </p>
        <div className="h-3" />
        <LoginButton />
      </div>
    </main>
  );
}
