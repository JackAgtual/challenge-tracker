"use client";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar";
import Link from "next/link";

type MenuLink = {
  groupLabel: string;
  menuItems: {
    name: string;
    href: string;
    anchorTag?: boolean;
  }[];
};

export function AppSidebar() {
  const { isMobile, setOpenMobile } = useSidebar();

  function handleNavClick() {
    if (isMobile) {
      setOpenMobile(false);
    }
  }

  const links: MenuLink[] = [
    {
      groupLabel: "Challenges",
      menuItems: [
        { href: "/dashboard", name: "Dashboard" },
        { href: "/challenges/create", name: "Create" },
      ],
    },
    {
      groupLabel: "Account",
      menuItems: [
        { href: "/account-setup", name: "Edit" },
        { href: "/auth/logout", name: "Logout", anchorTag: true },
      ],
    },
  ];

  return (
    <Sidebar>
      <SidebarHeader>Challenge Tracker</SidebarHeader>
      <SidebarContent>
        {links.map(({ groupLabel, menuItems }) => (
          <SidebarGroup key={groupLabel}>
            <SidebarGroupLabel>{groupLabel}</SidebarGroupLabel>
            <SidebarMenu>
              {menuItems.map(({ href, name, anchorTag }) => (
                <SidebarMenuItem key={href}>
                  <SidebarMenuButton
                    render={
                      anchorTag ? (
                        <a href={href}>{name}</a>
                      ) : (
                        <Link href={href} onClick={handleNavClick}>
                          {name}
                        </Link>
                      )
                    }
                  />
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroup>
        ))}
      </SidebarContent>
    </Sidebar>
  );
}
