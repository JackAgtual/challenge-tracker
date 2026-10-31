import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { Toaster } from "@/components/ui/toast";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { links } from "@/lib/links";
import { getInitials } from "@/lib/name-utils";
import Link from "next/link";
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
          <DropdownMenu>
            <DropdownMenuTrigger
              nativeButton={false}
              render={
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
              }
            />
            <DropdownMenuContent>
              <DropdownMenuGroup>
                <DropdownMenuItem
                  render={
                    <Link href={links.editAccount.href}>
                      {links.editAccount.name}
                    </Link>
                  }
                />
                <DropdownMenuItem
                  render={<a href={links.logout.href}>{links.logout.name}</a>}
                />
              </DropdownMenuGroup>
            </DropdownMenuContent>
          </DropdownMenu>
        </header>
        <div className="mx-5">{children}</div>
      </main>
      <Toaster />
    </SidebarProvider>
  );
}
