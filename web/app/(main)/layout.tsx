import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { Toaster } from "@/components/ui/toast";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { getInitials } from "@/lib/name-utils";
import { AppSidebar } from "./components/AppSidebar";

export default async function Layout({
  children,
}: {
  children: React.ReactNode;
}) {
  await getValidSession();

  const { data, error } = await client.GET("/users/me");

  return (
    <SidebarProvider>
      <AppSidebar />
      <main className="w-full">
        <header className="flex justify-between py-2 px-5 mb-5 border-b-2">
          <SidebarTrigger size="icon-lg" />
          <Avatar>
            {error ? (
              <AvatarFallback>?</AvatarFallback>
            ) : (
              <>
                <AvatarImage src={data.avatarSrc} />
                <AvatarFallback>{getInitials(data)}</AvatarFallback>
              </>
            )}
          </Avatar>
        </header>
        <div className="mx-5">{children}</div>
      </main>
      <Toaster />
    </SidebarProvider>
  );
}
