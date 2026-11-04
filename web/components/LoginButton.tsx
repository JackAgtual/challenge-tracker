import { links } from "@/lib/links";
import { Button } from "./ui/button";

export function LoginButton() {
  return (
    <Button>
      <a href={links.login.href}>Login</a>
    </Button>
  );
}
